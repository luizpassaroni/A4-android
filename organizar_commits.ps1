[CmdletBinding()]
param()

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Write-Step {
    param(
        [int]$Number,
        [string]$Description
    )

    Write-Host ""
    Write-Host ("=" * 78) -ForegroundColor DarkGray
    Write-Host ("ETAPA {0}: {1}" -f $Number, $Description) -ForegroundColor Cyan
    Write-Host ("=" * 78) -ForegroundColor DarkGray
}

function Invoke-Git {
    param(
        [Parameter(Mandatory)]
        [string[]]$Arguments
    )

    & git @Arguments
    if ($LASTEXITCODE -ne 0) {
        throw "Falha ao executar: git $($Arguments -join ' ')"
    }
}

function Add-And-Commit {
    param(
        [int]$Number,
        [string]$Description,
        [string]$Message,
        [string[]]$Paths
    )

    Write-Step -Number $Number -Description $Description
    Write-Host "Adicionando arquivos ao staging..." -ForegroundColor Yellow
    Invoke-Git -Arguments (@("add", "--") + $Paths)

    & git diff --cached --quiet --exit-code
    $diffExitCode = $LASTEXITCODE
    if ($diffExitCode -eq 0) {
        Write-Host "Nenhuma alteracao para esta etapa. Commit ignorado." -ForegroundColor DarkYellow
        return
    }
    if ($diffExitCode -ne 1) {
        throw "Nao foi possivel verificar o staging da etapa $Number."
    }

    Write-Host "Arquivos preparados para o commit:" -ForegroundColor Green
    Invoke-Git -Arguments @("diff", "--cached", "--name-status")

    Write-Host "Criando commit: $Message" -ForegroundColor Yellow
    Invoke-Git -Arguments @("commit", "-m", $Message)
}

$repositoryRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Push-Location -LiteralPath $repositoryRoot

try {
    Write-Host "Validando repositorio Git em: $repositoryRoot" -ForegroundColor Cyan
    Invoke-Git -Arguments @("rev-parse", "--is-inside-work-tree")

    $previousErrorActionPreference = $ErrorActionPreference
    $ErrorActionPreference = "Continue"
    & git rev-parse --verify --quiet HEAD *> $null
    $headExitCode = $LASTEXITCODE
    $ErrorActionPreference = $previousErrorActionPreference

    if ($headExitCode -eq 0) {
        throw "Este script foi criado para organizar o historico inicial e deve ser executado antes do primeiro commit."
    }
    if ($headExitCode -ne 1) {
        throw "Nao foi possivel verificar se o repositorio possui commits."
    }

    $userName = (& git config --get user.name)
    $userEmail = (& git config --get user.email)
    if ([string]::IsNullOrWhiteSpace($userName) -or [string]::IsNullOrWhiteSpace($userEmail)) {
        throw "Configure user.name e user.email no Git antes de executar o script."
    }

    Write-Host "Limpando somente o staging inicial, sem excluir arquivos locais..." -ForegroundColor Yellow
    Invoke-Git -Arguments @("rm", "--cached", "-r", "-f", "--ignore-unmatch", "--", ".")

    Add-And-Commit `
        -Number 1 `
        -Description "Infraestrutura base" `
        -Message "chore: inicializa estrutura base do projeto e dependencias (Room, Retrofit, ViewBinding)" `
        -Paths @(
            ".gitignore",
            "organizar_commits.ps1",
            "app/.gitignore",
            "build.gradle.kts",
            "app/build.gradle.kts",
            "settings.gradle.kts",
            "gradle.properties",
            "gradle/",
            "gradlew",
            "gradlew.bat",
            "app/src/main/AndroidManifest.xml",
            "app/src/main/keepRules/",
            "app/src/test/",
            "app/src/androidTest/"
        )

    Add-And-Commit `
        -Number 2 `
        -Description "Modelo de dados e enums" `
        -Message "feat: mapeia modelo relacional do banco de dados em entidades Java e enums" `
        -Paths @(
            "app/src/main/java/com/trigodourado/app/data/model/"
        )

    Add-And-Commit `
        -Number 3 `
        -Description "Persistencia local com Room" `
        -Message "feat: configura banco de dados SQLite local utilizando a biblioteca Room" `
        -Paths @(
            "app/src/main/java/com/trigodourado/app/data/local/"
        )

    Add-And-Commit `
        -Number 4 `
        -Description "Rede, Retrofit, ViaCEP e repositories" `
        -Message "feat: integra cliente HTTP Retrofit e implementa auto-preenchimento via ViaCEP com RN01" `
        -Paths @(
            "app/src/main/java/com/trigodourado/app/data/api/",
            "app/src/main/java/com/trigodourado/app/data/repository/",
            "app/src/main/java/com/trigodourado/app/util/CepMaskUtil.java"
        )

    # Os ViewModels deste projeto estao distribuidos dentro dos pacotes ui.
    Add-And-Commit `
        -Number 5 `
        -Description "ViewModels e estados reativos" `
        -Message "feat: implementa ViewModels reativas para controle de estado do carrinho e login" `
        -Paths @(
            ":(glob)app/src/main/java/com/trigodourado/app/ui/**/*ViewModel.java",
            ":(glob)app/src/main/java/com/trigodourado/app/ui/**/*State.java",
            "app/src/main/java/com/trigodourado/app/util/SessionManager.java"
        )

    Add-And-Commit `
        -Number 6 `
        -Description "Layouts e recursos visuais Material Design 3" `
        -Message "feat: constroi interfaces visuais em XML baseadas em Material Design 3" `
        -Paths @(
            "app/src/main/res/layout/",
            "app/src/main/res/values/",
            "app/src/main/res/values-night/",
            "app/src/main/res/drawable/",
            "app/src/main/res/anim/",
            "app/src/main/res/xml/",
            ":(glob)app/src/main/res/mipmap-*/**"
        )

    Add-And-Commit `
        -Number 7 `
        -Description "Adapters, Activities, Fragments e navegacao" `
        -Message "feat: finaliza fatias verticais de telas (Login, Cardapio, Carrinho, Checkout e Dashboard)" `
        -Paths @(
            "app/src/main/java/com/trigodourado/app/ui/"
        )

    Write-Step -Number 8 -Description "Correcoes de responsividade, safe area e arquivos restantes"
    Write-Host "Executando git add . para preparar qualquer modificacao restante..." -ForegroundColor Yellow
    Invoke-Git -Arguments @("add", "--", ".")

    & git diff --cached --quiet --exit-code
    $remainingExitCode = $LASTEXITCODE
    if ($remainingExitCode -eq 1) {
        Write-Host "Arquivos preparados para o commit final:" -ForegroundColor Green
        Invoke-Git -Arguments @("diff", "--cached", "--name-status")
        Invoke-Git -Arguments @(
            "commit",
            "-m",
            "fix: aplica window insets para telas edge-to-edge e resolve bugs de icones M3"
        )
    }
    elseif ($remainingExitCode -eq 0) {
        Write-Host "Nenhuma alteracao restante para o commit final." -ForegroundColor DarkYellow
    }
    else {
        throw "Nao foi possivel verificar o staging da etapa final."
    }

    Write-Host ""
    Write-Host "Sequencia concluida. Historico criado:" -ForegroundColor Green
    Invoke-Git -Arguments @("log", "--oneline", "--decorate", "--reverse")

    Write-Host ""
    Write-Host "Status final do repositorio:" -ForegroundColor Green
    Invoke-Git -Arguments @("status", "--short", "--branch")
}
catch {
    Write-Host ""
    Write-Host "ERRO: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "A execucao foi interrompida. Revise o status antes de continuar:" -ForegroundColor Yellow
    & git status --short --branch
    exit 1
}
finally {
    Pop-Location
}

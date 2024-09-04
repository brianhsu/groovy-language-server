# Groovy Language Server

We use Java to write our produce code and use Groovy and spock framework to do unit test, and we heavily rely on Maven do pull our dependencies. Please be aware this is more likely a hack, I'm not expert on LSP nor groovy-ls, I just put a bunch of random code to make it work with my need. It might not work for you and the code quality probably is not that good.

This fork add the following two things to the original version, so vim-lsp could work with our project more easily.

1. It will add the dependency jar file classpath from `pom.xml` under your projects root.
2. You can add 'targets' folder to the classpath, so class built from Java (for example, mvn compile) could be found by Grooy Language Server.

For example, here is my lspconfig:

```lua
require('lspconfig')['groovyls'].setup{
    cmd = { 'java', '-jar', groovy_language_server_path },
    capabilities = cmp_capabilities,
    settings = {
        groovy = {
            targetFolder = {'/home/brianhsu/HelloGroovy/target/classes/'}
        }
    },
    root_dir = function ()
        local project_root = vim.fs.dirname(vim.fs.find({'gradlew', '.git', 'mvnw', 'pom.xml'}, { upward = true })[1])
        if project_root == nil then
            return vim.fn.getcwd()
        else
            return project_root
        end
    end
}
```

------

A [language server](https://microsoft.github.io/language-server-protocol/) for [Groovy](http://groovy-lang.org/).

The following language server protocol requests are currently supported:

- completion
- definition
- documentSymbol
- hover
- references
- rename
- signatureHelp
- symbol
- typeDefinition

The following configuration options are supported:

- groovy.java.home (`string` - sets a custom JDK path)
- groovy.classpath (`string[]` - sets a custom classpath to include _.jar_ files)

## Build

To build from the command line, run the following command:

```sh
./gradlew build
```

This will create _build/libs/groovy-language-server-all.jar_.

## Run

To run the language server, use the following command:

```sh
java -jar groovy-language-server-all.jar
```

Language server protocol messages are passed using standard I/O.

## Editors and IDEs

A sample language extension for Visual Studio Code is available in the _vscode-extension_ directory. There are no plans to release this extension to the VSCode Marketplace at this time.

Instructions for setting up the language server in Sublime Text is available in the _sublime-text_ directory.

Moonshine IDE natively provides a Grails project type that automatically configures the language server.

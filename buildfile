require 'buildr/git_auto_version'
require 'buildr/gpg'

PROVIDED_DEPS = [:javaee_api, :javax_annotation]
KEYCLOAK_DEPS = [:keycloak_adapter_core, :keycloak_adapter_spi, :keycloak_core, :keycloak_common]

desc 'A simple service interface and base classes to be used by keycloak secured services'
define 'simple-keycloak-service' do
  project.group = 'org.realityforge.keycloak.sks'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'
  compile.options.warnings = true
  compile.options.other = %w(-Werror -Xmaxerrs 10000 -Xmaxwarns 10000)

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/simple-keycloak-service')
  pom.add_developer('realityforge', 'Peter Donald')
  pom.provided_dependencies.concat PROVIDED_DEPS

  compile.with PROVIDED_DEPS,
               KEYCLOAK_DEPS

  test.using :testng
  test.with :mockito, :guiceyloops

  package(:jar)
  package(:sources)
  package(:javadoc)

  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.add_code_insight_settings
  ipr.add_nullable_manager
  ipr.add_javac_settings('-Xlint:all -Werror -Xmaxerrs 10000 -Xmaxwarns 10000')
end

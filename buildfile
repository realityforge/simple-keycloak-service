require 'buildr/git_auto_version'
require 'buildr/gpg'

PROVIDED_DEPS = [:javaee_api, :javax_annotation]

desc 'A simple service interface and base classes to be used by keycloak secured services'
define 'simple-keycloak-service' do
  project.group = 'org.realityforge.keycloak.sks'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/simple-keycloak-service')
  pom.add_developer('realityforge', 'Peter Donald')
  pom.provided_dependencies.concat PROVIDED_DEPS

  compile.with PROVIDED_DEPS,
               :keycloak_adapter_core,
               :keycloak_adapter_spi,
               :keycloak_core,
               :keycloak_common

  test.using :testng
  test.with :mockito, :guiceyloops

  package(:jar)
  package(:sources)
  package(:javadoc)

  iml.add_jruby_facet
end

<!-- ## [Unreleased] -->
<!-- ### Breaking changes ⚠ -->
<!-- ### Added ✔️-->
<!-- ### Changed 🛠️-->
<!-- ### Deprecated 🛑-->
<!-- ### Removed 🗑️-->
<!-- ### Fixed 🐛-->
<!-- ### Security 🛡️-->
<!-- Este fichero sigue el formato de https://keepachangelog.com -->


## [Unreleased]
## [3.15.1] - 2025-04-11
### Changed 🛠️
* **EE Version**: Update Ontimize EE version to 5.12.1
## [3.15.0] - 2025-03-12
### Added ✔️
* **Multi tenant**: Allow to configure tenants using the preferences.
* **DefaultSecurityAutoConfiguration**: Allow to use SSL in LDAP connections.
### Fixed 🐛
* **ODMSAutoConfigure**: Allows the dmsColumnHelper to get the nameConvention from the application.
## [3.14.0] - 2024-02-23
### Added ✔️
* **JaCoCo**: Add JaCoCo coverage on Sonar
* **Multi tenant**: Ask the tenant provider to load a tenant when it can not be found.
### Changed 🛠️
* **Sonar**: Upgrade Java version on Sonar action
### Fixed 🐛
* **OReportAutoConfigure**: Removed requirement for preferences to use the reporting system. Although on-demand reports require it, it is not necessary for the storage of reports, so the requirement that it must always exist is removed.
* **Multi tenant:** Fix an authorization issue when accessing to the application root path
* **JdbcAutoConfiguration:** Added configuration for the MySQL SQLStatementHandler in the application.yml
* **Sonar**: Solve some issues.
## [3.13.0] - 2023-11-06
### Added ✔️
* **Keycloak**: Allow to use the Keycloak authentication on non-http calls
## [3.12.0] - 2023-08-01
### Changed 🛠️
* **[SDMS](https://github.com/ontimize/ontimize-jee-sdms/blob/develop/CHANGELOG.md#131---2023-06-28)**: Ontimize EE SDMS has been updated to version `1.3.1`.
* **[Ontimize EE Report](https://github.com/ontimize/ontimize-jee-report/blob/develop/CHANGELOG.md#330---2023-07-31)**: Ontimize EE Report has been updated to version `3.3.0`.
* **Changelog**: The structure of the CHANGELOG.md file has been modified so that it follows the structure shown at [keepachangelog](https://keepachangelog.com/).
## [3.11.0] - 2023-06-12
### Added ✔️
* **[SDMS](https://ontimize.github.io/docs/v3/systems/sdms/)**: Add Ontimize SDMS (Amazon S3) support.
* **Keycloak**: Allow to configure roles without a database.
<!-- ### 🔧 Bug fixes: -->
## [3.10.1] - 2023-04-21
### Fixed 🐛
* **Versions:** Remove SNAPSHOT tag from versions
## [3.10.0] - 2023-04-20
### Added ✔️
* **Keycloack and Multi tenant**: Unlink keycloak from multitenant functionality.
<!-- ### 🔧 Bug fixes: -->
## [3.9.0] - 2023-01-05
### Added ✔️
* **Keycloak:** Keycloak added to enable single sign-on to applications and services.
* **Multi-tenant:** Multitennat capability is added for applications.
* **Async Tasks:** The possibility to create asynchronous tasks is added. This is required for data export and on-demand reporting.
* **[Multiple DataSources](https://ontimize.github.io/docs/v3/basics/multipledatasources/):** Multiple data sources are allowed for the application to work with.
* **[Dynamic Report](https://ontimize.github.io/docs/v3/systems/reports/report-on-demand):** Allow the final users  define, view and store reports from any table available in the application.
* **[Export services](https://ontimize.github.io/docs/v3/basics/exportdata/):** System to export the DAO data of a service and dump it directly to a file.


[unreleased]: https://github.com/ontimize/ontimize-boot/compare/3.15.1...HEAD
[3.15.1]: https://github.com/ontimize/ontimize-boot/compare/3.15.0...3.15.1
[3.15.0]: https://github.com/ontimize/ontimize-boot/compare/3.14.0...3.15.0
[3.14.0]: https://github.com/ontimize/ontimize-boot/compare/3.13.0...3.14.0
[3.13.0]: https://github.com/ontimize/ontimize-boot/compare/3.12.0...3.13.0
[3.12.0]: https://github.com/ontimize/ontimize-boot/compare/3.11.0...3.12.0
[3.11.0]: https://github.com/ontimize/ontimize-boot/compare/3.10.1...3.11.0
[3.10.1]: https://github.com/ontimize/ontimize-boot/compare/3.10.0...3.10.1
[3.10.0]: https://github.com/ontimize/ontimize-boot/compare/3.9.0...3.10.0
[3.9.0]: https://github.com/ontimize/ontimize-boot/compare/3.8.1...3.9.0

# comptandye_backend
Source of the Backend code

## Build the WAR for Production
`mvn clean install -Pprod`

## Build the WAR for Dev
`mvn clean install`

## Checks dependencies/plugins version
`mvn versions:dependency-updates-report versions:plugin-updates-report versions:property-updates-report`

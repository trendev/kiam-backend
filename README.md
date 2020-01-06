# comptandye-backend [![CircleCI](https://circleci.com/gh/trendev/comptandye-backend.svg?style=svg&circle-token=d43206b685c13578091239ad4c93d81bca3ae4df)](https://circleci.com/gh/trendev/comptandye-backend)
Source of the Backend code

## Build the WAR for Production
`mvn clean install -Pprod`

## Build the WAR for Dev
`mvn clean install`

## Checks dependencies/plugins version
`mvn versions:dependency-updates-report versions:plugin-updates-report versions:property-updates-report`
... and go in target/site folder to show the reports
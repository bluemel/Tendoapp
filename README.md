Tendoapp
========

A simple CRUD application example that demonstrates the usage of the Google Web Toolkit
URL:   http://tendo-app.appspot.com/

Admin: https://cloud.google.com/products/app-engine/
       https://cloud.google.com/console/project

Troublshooting:

- Datanucleus byte code enhancement fails
  - Project / Properties / Google / App Engine / ORM
    kontrollieren
  - Eclipse neu starten + Project clean
  - Eventuell ohne Proxy verbinden

- Deploy transaction is interrupted
  Rollback the appengine deploy transaction
  [C:\Projects\GWT\tools\eclipse\4.3.0\]
    plugins\com.google.appengine.eclipse.sdkbundle_1.8.7\appengine-java-sdk-1.8.7\bin\appcfg.cmd
    -p localhost:9200 rollback C:\Projects\GWT\Tendoapp\war

# vertx-k8s-configmap
Example of consuming a kubernetes configmap as a vertx-config json config

Created due to a lack of examples available online.

Instructions:

Load your config.json into a k8s configmap:
`kubectl -n $NAMESPACE create configmap $CONFIGNAME --from-file=config.json`

Set values for namespace and configname in ConfigStoreOptions MainVerticle lines 25 to 27.

`mvn clean package`

`docker push mjad/vertx-k8s-configmap:0.1 $yourregistry`

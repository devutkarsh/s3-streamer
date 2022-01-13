#  S3 Streamer
## A java based container microservice to read S3 objects via streaming using a Http localhost endpoint.
This container service run on localhost:9999 (default port) where the S3 objects can be accssed locally via streaming over http protocol.
#### Endpoint : http://localhost:9999/<bucket_name>/<object_key> or http://s3-streamer:9999/<bucket_name>/<object_key>

### Docker
docker pull devutkarsh/s3-streamer

## Build Steps
- Pull git project to your local
- Update port or application properties if required
- Build the Java project and docker image using gradle
- Push jar to docker repository
- Deploy to container using the docker image



### Installation Guide
Requires Java 8 or higher and gradle -

```sh
cd s3-streamer
./gradlew clean build distdocker
aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin <docker_repo_path>
docker tag <local-docker-image_id> <docker_repo_path>
docker push <docker_repo_path>
```

## Deploy kubernetes container
```
kubectl apply -f s3-streamer.yaml
```

### Verify if your pod is running
```
kubectl get pods
kubectl logs <s3-streamer-pod-name>
```

More on https://blog.devutkarsh.com/streaming-aws-s3-objects-in-aws-eks-cluster/

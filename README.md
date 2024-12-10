## Mono Repo
This is combination of 
Tejal2002's mouth shut api
and Kedar59's true caller api
We will be using Kedar59's project to replace Tejal2002's User API's

## Redis local Setup

### Windows
Redis isnt fully supported on windows. So install docker then run a docker image of redis-stack.
```
$ docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:latest
```
### MacOS
Run the following commands in terminal.
I dont have a macOS machine so haven't tried them.
```
# install redis-stack
$ brew tap redis-stack/redis-stack
$ brew install --cask redis-stack

# start redis-stack
$ redis-stack-server
```
### Linux
These commands are for Ubuntu >= 16 and Debian >=11
```
curl -fsSL https://packages.redis.io/gpg | sudo gpg --dearmor -o /usr/share/keyrings/redis-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/redis-archive-keyring.gpg] https://packages.redis.io/deb $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/redis.list
sudo apt-get update
sudo apt-get install redis-stack-server
```
For more info check out getting started [Guide](https://redis.io/learn/howtos/quick-start)

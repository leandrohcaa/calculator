docker ps --filter status=exited -q | xargs docker rm & docker volume prune -f & docker compose up
# Build stage
FROM node:22-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build --configuration=production

# Run stage
FROM nginx:alpine
COPY --from=build app/dist/images-logs/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

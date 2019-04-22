FROM ruby:2.5.5

RUN apt-get update -qq && apt-get install -y \
build-essential

RUN apt-get install -y default-libmysqlclient-dev

RUN mkdir -p /app
WORKDIR /app

COPY Gemfile Gemfile.lock ./
RUN gem install bundler && bundle install
RUN rails active_storage:install

COPY . ./

EXPOSE 3000

CMD ["rails", "s", "-e", "production"]

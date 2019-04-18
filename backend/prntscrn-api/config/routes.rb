# TODO: Scaffold setup not manual routes
Rails.application.routes.draw do
  get "auth_test", to: "user#auth_test"
  get "admin_test", to: "user#admin_test"

  resources :user, only: [:index, :create, :destroy]

  post "login", to: "user#login"
end

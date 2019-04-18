# TODO: Scaffold setup not manual routes
Rails.application.routes.draw do
  get "auth_test", to: "user#auth_test"
  get "admin_test", to: "user#admin_test"

  post "login", to: "user#login"

  resources :user, only: [:index, :create, :destroy]
  resources :image, only:[:index, :show, :create, :destroy]
end

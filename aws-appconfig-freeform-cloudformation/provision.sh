APP_NAME="order"
COST_CENTER="sbk"

aws cloudformation deploy --template-file ./app-config.cfn.yaml \
  --stack-name ${APP_NAME} \
  --parameter-overrides \
  AppName=${APP_NAME} \
  CostCenter=${COST_CENTER} \
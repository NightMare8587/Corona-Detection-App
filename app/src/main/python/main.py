import numpy as np
from keras.preprocessing import image
from keras.models import load_model
def detect_corona(image1):
  #print('fetching results...')
  new_model = load_model('Covid_model.h5')
  test_image = image.load_img(image1,target_size=(224,224))
  test_image = image.img_to_array(test_image)
  test_image = np.expand_dims(test_image, axis = 0)

  result = new_model.predict(test_image)
  y_pred = np.argmax(result, axis=1)
  #print('Model prediction: ',result)
  if result[0][0]<result[0][1] and result[0][0]>4.226988e-15:
    #prediction = 'Patient is affected with COVID-19'
    return 1
  else:
    #prediction = 'Patient is Healthy'
    return 0
  #print('YPred',y_pred)
  #print('===================================')
  #print(prediction)
  #print('===================================')

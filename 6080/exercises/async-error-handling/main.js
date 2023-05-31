const API_URL = 'https://jsonplaceholder.typicode.com'

function getUserByIdAndUsername(id, username) {
  return fetch(API_URL + '/users/' + id)
  .then((response)=> {
    if (response.status === 404) {
      return Promise.reject
    }
  })
  .catch()
  .finally(()=>{
    console.log("here")
  })
}

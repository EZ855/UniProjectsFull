const API_URL = 'https://jsonplaceholder.typicode.com'

function getPostAndUser(postId) {
  return fetch(`${API_URL}/users/${postId}`)
    .then(response => {
      // You can use `Promise.reject` or `throw` to trigger  `.catch`
      if (response.status === 404) {
        return Promise.reject(new Error(`Could not find with id ${id}`))
      } else if (!response.ok) {
        throw new Error(`Something went wrong when getting user with id ${id}`)
      }

      return response.json()
    })
    .then(user => {
      if (user.username !== username) {
        throw new Error(
          `The user with id ${id} does not have username '${username}'`
        )
      }
      return user
    })
}

getPostAndUser(1).then(result => {
  console.log(result)
})

function getAllPosts() {
  return Promise.resolve()
}

getAllPosts().then(posts => {
  console.log(posts)
})

// NOTE: These functions were copied from our assignment 3 submission.

// Function used to call the fetch API and retrieve data from the backend.
export function fetchAPI (path, options) {
  return new Promise((resolve, reject) => {
    fetch(`http://localhost:5005/${path}`, options)
      .then((result) => {
        // Process the response if no errors occur.
        if (result.ok) {
          result.json()
          // The json() function returns a promise, so we need to resolve it.
            .then(result => {
              resolve(result);
            });
          // Otherwise return the error message in the 'error' field - for 4** errors.
        } else {
          result.json()
            .then((errorResponse) => {
              reject(errorResponse.error);
            });
        }
      })
    // Log 5** server errors on the console.
      .catch((error) => console.log('Error: ', error));
  })
}

// Function used to set up the optional parameter sent in the fetch request.
export function fetchSetup (method, path, token, body) {
  const options = {
    method,
    headers: { 'Content-Type': 'application/json' },
  };

  // Convert the included body to a JSON string and add it to options.
  if (body !== null) options.body = JSON.stringify(body);

  // Add the token to the options object if it is included.
  if (token !== null) options.headers.Authorization = `Bearer ${token}`;

  // Call the fetch API with the inputs given and return the result.
  return fetchAPI(path, options);
}

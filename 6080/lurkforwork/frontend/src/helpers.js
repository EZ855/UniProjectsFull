/**
 * Given a js file object representing a jpg or png image, such as one taken
 * from a html file input element, return a promise which resolves to the file
 * data as a data url.
 * More info:
 *   https://developer.mozilla.org/en-US/docs/Web/API/File
 *   https://developer.mozilla.org/en-US/docs/Web/API/FileReader
 *   https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs
 * 
 * Example Usage:
 *   const file = document.querySelector('input[type="file"]').files[0];
 *   console.log(fileToDataUrl(file));
 * @param {File} file The file to be read.
 * @return {Promise<string>} Promise which resolves to the file as a data url.
 */
export function fileToDataUrl(file) {
    const validFileTypes = [ 'image/jpeg', 'image/png', 'image/jpg' ]
    const valid = validFileTypes.find(type => type === file.type);
    // Bad data, let's walk away.
    if (!valid) {
        throw Error('provided file is not a png, jpg or jpeg image.');
    }
    
    const reader = new FileReader();
    const dataUrlPromise = new Promise((resolve,reject) => {
        reader.onerror = reject;
        reader.onload = () => resolve(reader.result);
    });
    reader.readAsDataURL(file);
    return dataUrlPromise;
}

// Function used to call the fetch API and retrieve data from the backend.
export function fetch_api(path, options) {
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
                .then((error_response) => {
                    reject(error_response['error']);
                });
            }
        })
        // Log 5** server errors on the console.
        .catch((error) => console.log('Error: ', error));
    })
};

// Function used to set up the optional parameter sent in the fetch request.
export function fetch_setup(method, path, token, body) {

    let options = {
        method: method,
        headers: {'Content-Type': 'application/json'},
    };

    // Convert the included body to a JSON string and add it to options.
    if (body !== null) options.body = JSON.stringify(body);

    // Add the token to the options object if it is included.
    if (token !== null) options.headers['Authorization'] = `Bearer ${token}`;

    // Call the fetch API with the inputs given and return the result.
    return fetch_api(path, options);
};
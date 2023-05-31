// TODO
function question1() {
  const microsoftRepos = getCompanyRepos("microsoft");
  const googleRepos = getCompanyRepos("google");
  const canvaRepos = getCompanyRepos("canva");

  Promise.all([microsoftRepos, googleRepos, canvaRepos])
  .then((companies) =>
    companies.forEach(company=>
      company.forEach((repo) => console.log(repo))
  ));
}

async function question2() {
  const microsoftRepos = getCompanyRepos("microsoft");
  const googleRepos = getCompanyRepos("google");
  const canvaRepos = getCompanyRepos("canva");

  try{
    const companies = await Promise.all([
      microsoftRepos, 
      googleRepos, 
      canvaRepos
    ])
    
    companies.forEach(company=>
      company.forEach((repo) => 
        console.log(repo)
      )
    );

  }
  catch(error){
    console.log(error);
  }
  
}
function question4() {
  const microsoftRepos = getCompanyRepos("microsoft");
  const googleRepos = getCompanyRepos("google");
  const fakeRepo = getCompanyRepos("fake");

  Promise.allSettled([microsoftRepos, googleRepos, canvaRepos])
  .then((results)=>{
    items.forEach(item=>{
      if (item['status']) {
        
      }
    })
  })
}

/**
 * Fetches public repo information from GitHub for a specific company
 * @param {String} companyName Company name
 * @returns A list of repo names belonging to the company
 */
async function getCompanyRepos(companyName) {
  // Since fetch isn't in NodeJS, use a library to polyfill it
  const fetch = require("node-fetch");

  // Fetch the company's repos from GitHub
  const response = await fetch(
    `https://api.github.com/orgs/${companyName}/repos`
  );
  const data = await response.json();

  // If the response is not OK, throw an error
  // This is the same as Promise.reject() in non-async functions
  if (response.status !== 200) {
    throw new Error(`Response of ${response.status} for ${companyName}`);
  }

  // Push the full names of each repo into an array
  const repoArray = [];
  data.forEach((repo) => repoArray.push(repo.full_name));

  return repoArray;
}

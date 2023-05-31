

let streetName = document.getElementById('street-name');
let suburb = document.getElementById('suburb');
let postcode = document.getElementById('postcode');
let dob = document.getElementById('dob');
let buildingType = document.getElementById('building-type');
let heating = document.getElementById('heating');
let airCon = document.getElementById('air-con');
let pool = document.getElementById('pool');
let sandpit = document.getElementById('sandpit');
let textArea = document.getElementById('text-area');

function update() {
    
    const form = document.forms["user-form"];

    let dateSplit = form.elements["dob"].value.split("/");
    var re = new RegExp("[0-9]{2}/[0-9]{2}/[0-9]{4}");
    var parsedDate;
    if (dateSplit.length == 3) {
        var dateAcceptableForm = dateSplit[1]+"-"+dateSplit[0]+"-"+dateSplit[2];
        parsedDate = new Date(dateAcceptableForm);
    }

    if (form.elements["street-name"].value.length < 3 || form.elements["street-name"].value.length > 50) {
        textArea.innerText = "Please input a valid street name";
    }
    else if (form.elements["suburb"].value.length < 3 || form.elements["suburb"].value.length > 50) {
        textArea.innerText = "Please input a valid suburb";
    }
    else if (form.elements["postcode"].value.length != 4) {
        textArea.innerText = "Please input a valid postcode";
    }
    else if (!re.test(form.elements["dob"].value) || (parsedDate instanceof Date && isNaN(parsedDate))) {
        textArea.innerText = "Please input a valid date of birth";
    }
    else {
        var today = new Date();
        var age = today.getFullYear() - parsedDate.getFullYear();
        var m = today.getMonth() - parsedDate.getMonth();
        if (m < 0 || (m === 0 && today.getDate() < parsedDate.getDate())) {
            age--;
        }
        let buildingTypeString;
        if (buildingType.value == "apartment") {
            buildingTypeString = "an apartment";
        }
        else {
            buildingTypeString = "a house";
        }
        let featuresCount = 0;
        let featuresString;
        if (heating.checked) {
            featuresString = heating.value;
            featuresCount++;
        }
        if (airCon.checked) {
            if (featuresCount == 0) {
                featuresString = airCon.value;
            }
            else {
                featuresString += ", " + airCon.value;
            }
            featuresCount++;
        }
        if (pool.checked) {
            if (featuresCount == 0) {
                featuresString = pool.value;
            }
            else {
                featuresString += ", " + pool.value;
            }
            featuresCount++;
        }
        if (sandpit.checked) {
            if (featuresCount == 0) {
                featuresString = sandpit.value;
            }
            else {
                featuresString += ", " + sandpit.value;
            }
            featuresCount++;
        }
        if (featuresCount == 0) {
            featuresString = "no features";
        }
        textArea.innerText = "Your are "+age+" years old, and your address is "+streetName.value+" St, "+suburb.value+", "+postcode.value+", Australia. Your building is "+buildingTypeString+", and it has " + featuresString + ".";
    }
}

function isAllSelected (){
    if (heating.checked && airCon.checked && pool.checked && sandpit.checked) {
        document.getElementById('select-all').style.display = "none";
        document.getElementById('deselect-all').style.display = "inline";
    }
    else {
        document.getElementById('select-all').style.display = "inline";
        document.getElementById('deselect-all').style.display = "none";
    }
}

document.forms["user-form"].addEventListener ('reset', ()=> {
    textArea.innerText = "";
});
document.getElementById('select-all').addEventListener('click', ()=>{
    heating.checked = true;
    airCon.checked = true;
    pool.checked = true;
    sandpit.checked = true;
    document.getElementById('select-all').style.display = "none";
    document.getElementById('deselect-all').style.display = "inline";
    update();
});
document.getElementById('deselect-all').addEventListener('click', ()=>{
    heating.checked = false;
    airCon.checked = false;
    pool.checked = false;
    sandpit.checked = false;
    document.getElementById('deselect-all').style.display = "none";
    document.getElementById('select-all').style.display = "inline";
    update();
});

streetName.addEventListener('blur', () => {
    update();
});
suburb.addEventListener('blur', () => {
    update();
});
postcode.addEventListener('blur', () => {
    update();
});
dob.addEventListener('blur', () => {
    update();
});
buildingType.addEventListener('change', () => {
    update();
});
heating.addEventListener('change', () => {
    isAllSelected();
    update();
});
airCon.addEventListener('change', () => {
    isAllSelected();
    update();
});
pool.addEventListener('change', () => {
    isAllSelected();
    update();
});
sandpit.addEventListener('change', () => {
    isAllSelected();
    update();
});
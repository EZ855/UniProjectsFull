const body = document.getElementById('body');
const div = document.createElement('div');
div.style.background = "#cccccc";

const a = document.createElement('a');
a.target = "_blank";
a.href = "https://google.com";


const img = document.createElement('img');
img.src = "https://i.ytimg.com/vi/yJiVZUKAS84/maxresdefault.jpg";
img.alt = "Me and my sibling";

a.appendChild(img);
div.appendChild(a);
body.appendChild(div);
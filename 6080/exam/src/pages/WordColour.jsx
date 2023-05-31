import React from 'react'
import Sidebar from '../components/Sidebar'
import Footer from '../components/Footer'
import './WordColour.css'

export default function WordColour() {
  const colours = ["red", "blue", "orange", "yellow", "green", "purple", "pink"];
  function getRandomInt(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min) + min);
  }
  const [current, setCurrent] = React.useState("red");
  const [count, setCount] = React.useState(0);

  React.useEffect(()=>{
    setTimeout(()=> {
      document.getElementById("wc-text").style.display = "block";
      const colour = colours[getRandomInt(0,7)];
      setCurrent(colour);
      document.getElementById("wc-1").style.backgroundColor = colour;
      for (let index = 2; index < 5; index++) {
        let currColour = colours[getRandomInt(0,7)];
        while(currColour === colour) {
          currColour = colours[getRandomInt(0,7)];
        }
        document.getElementById(`wc-${index}`).style.backgroundColor = currColour;
      }
    }, 2000)
  }, [])
  
  const handleClick = (e) => {
    const win = count;
    setCount(count + 1);
    if (win + 1 === 3) {
      alert("You have won")
      localStorage.getItem("wins")
      localStorage.setItem("wins", localStorage.getItem("wins") - 1);
      setCount(0);

    }
    if (document.getElementById(e.target.id).style.backgroundColor === current) {
      const colour = colours[getRandomInt(0,7)];
      setCurrent(colour);
      document.getElementById("wc-1").style.backgroundColor = colour;
      for (let index = 2; index < 5; index++) {
        let currColour = colours[getRandomInt(0,7)];
        while(currColour === colour) {
          currColour = colours[getRandomInt(0,7)];
        }
        document.getElementById(`wc-${index}`).style.backgroundColor = currColour;
      }
    }
    
  }
  return (
    <div>
      <Sidebar/>
      <div className='dashboard-main-body'>
        <div className='wc-left'>
          <div id='wc-text'>
            {current}
          </div>
        </div>
        <div className='wc-right'>
          <div className='wc-right-half'>
            <div onClick={handleClick} className='wc-right-box' id='wc-1'>
            </div>
            <div onClick={handleClick} className='wc-right-box' id='wc-2'>
            </div>
          </div>
          <div className='wc-right-half'>
            <div onClick={handleClick} className='wc-right-box' id='wc-3'>
            </div>
            <div onClick={handleClick} className='wc-right-box' id='wc-4'>
            </div>
          </div>
        </div>
      </div>
      <Footer/>
    </div>
  )
}

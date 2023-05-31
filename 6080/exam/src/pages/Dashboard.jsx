import React from 'react'
import Sidebar from '../components/Sidebar'
import Footer from '../components/Footer'
import './Dashboard.css'

export default function Dashboard() {
    const [wins, setWins] = React.useState(404)
    React.useEffect(async ()=> {
        if (!localStorage.getItem("wins")) {
            const response = await fetch('https://cgi.cse.unsw.edu.au/~cs6080/raw/data/score.json');
            if (response.ok) {
                const responseJson = await response.json();
                setWins(responseJson.score);
                localStorage.setItem("original score", responseJson.score);
                localStorage.setItem("wins", parseInt(responseJson.score));
            }
            else {
                console.log("error fetching score!")
            }
        }
        else {
            setWins(localStorage.getItem("wins"));
            if (localStorage.getItem("wins") === 0) {
                alert("Congratulations!")
            }
        }
    }, [])

    const resetScore = () => {
        setWins(localStorage.getItem("original score"));
    }
  return (
    <div className='dashboard'>
        <Sidebar/>
        <div className='dashboard-main-body centre'>
            <div>
                Wins to achieve: {wins}
                <button onClick={resetScore}> reset </button>
            </div>
            <div className='dashboard-main-body-letsgo'>
                Let's go!
            </div>
        </div>
        <Footer/>
    </div>
  )
}

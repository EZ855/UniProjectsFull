import React from 'react'
import Sidebar from '../components/Sidebar'
import Footer from '../components/Footer'

export default function FindAWord() {
  const init = ["A", "B", "C", "D", "E", "F", "G"];
  return (
    <div>
      <Sidebar/>
      <div className='find-a-word'>
        {init.forEach(element1 => {
          <div className='faw-cell'>
            {element1}
          </div>
        })}
      </div>
      <Footer/>
    </div>
  )
}

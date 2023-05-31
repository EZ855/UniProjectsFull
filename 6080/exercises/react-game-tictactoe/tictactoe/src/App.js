import logo from './logo.svg';
import './App.css';
import { useState } from 'react';

function App() {

  const emptyBoard = [
    ['','',''],
    ['','',''],
    ['','',''],
  ];
  const [board, setBoard] = useState(emptyBoard);
  

  return (
    <div>
      {board.map((row, x) => {
        return (
          <div>
            {row.map((value, y)=> {
              return (
                <button>{value}</button>
              )
            })}
          </div>
        )
      })}
    </div>
  );
}

export default App;

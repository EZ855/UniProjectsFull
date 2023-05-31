import logo from './logo.svg';
import './App.css';

function App() {
  const date = new Date(Date.now());
  const style = {color: 'red'};
  return (
    <div className="App">
      {date.toLocaleString()}
    </div>
  );
}

export default App;

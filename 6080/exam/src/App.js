import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Dashboard from './pages/Dashboard';
import WordColour from './pages/WordColour';
import Frogger from './pages/Frogger';
import FindAWord from './pages/FindAWord';


function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Routes>
          <Route path="/home" element={<Dashboard />} />
          <Route path="/wordcolour" element={<WordColour />} />
          <Route path="/frogger" element={<Frogger />} />
          <Route path="/findaword" element={<FindAWord />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;

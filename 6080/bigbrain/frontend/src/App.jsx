import React from 'react';
import {
  BrowserRouter,
  Route,
  Routes,
} from 'react-router-dom';

import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import QuizEdit from './pages/QuizEdit';
import QuestionEdit from './pages/QuestionEdit';
import GameSettings from './pages/GameSettings';
import JoinGame from './pages/JoinGame';
import PlayGame from './pages/PlayGame';
import PlayerViewGameResults from './pages/PlayerViewGameResults';
import GameLobby from './pages/GameLobby';

function App () {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' Component={Login} />
        <Route path='/register' Component={Register} />
        <Route path='/dashboard' Component={Dashboard} />
        <Route path='/quiz/:id' Component={QuizEdit} />
        <Route path='/quiz/:quizId/question/:questionId' Component={QuestionEdit} />
        <Route path='/admin/game' Component={GameSettings} />
        <Route path='/game' Component={JoinGame} />
        <Route path='/play/game' Component={PlayGame} />
        <Route path='/play/game/results/:playerId' Component={PlayerViewGameResults} />
        <Route path='/play/game/lobby' Component={GameLobby} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

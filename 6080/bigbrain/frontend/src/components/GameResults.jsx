import React from 'react';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import { useLocation } from 'react-router-dom';
import { DataGrid } from '@mui/x-data-grid';
import { fetchSetup } from '../pages/helper.js';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip } from 'recharts';

const topStyle = {
  marginTop: '30px'
}

const lineStyle = {
  border: '1px solid black',
  width: '90%'
}

const entireStyle = {
  width: '100%',
  display: 'flex',
  alignItems: 'center'
}

export default function GameResults (props) {
  // Get the user token
  const token = localStorage.getItem('token');
  // Grab the search paramaters from the current URL
  const query = new URLSearchParams(useLocation().search);
  const [tableRows, setTableRows] = React.useState([]); // Used to create the table
  const [responseTimeAverage, setResponseTimeAverage] = React.useState([]); // Used to create the bar chart
  // Extract the sessionID
  const sessionID = query.get('sessionID');
  // Stores all of the results for the game
  const gameResult = {};
  // Used for adding arrays to the gameResult object
  let resultArray = [];
  // Used for adding objects to the gameResult object
  let individualResult = {};
  // Stores all of the scores for each player
  const scores = {}
  // Stores all of the response times for each question
  const responseTimes = {};
  // Required for responseTimes object
  let firstRun = true;
  // This is used to find all of the results
  React.useEffect(async () => {
    try {
      // First fetch the results for the session
      const response = await fetchSetup('GET', `admin/session/${sessionID}/results`, token, null);
      const results = response.results;
      // Run through each of the objects
      for (const player in results) {
        let questionNum = 1;
        let score = 0;
        // Then run through each question answered by the player
        for (const question in results[player].answers) {
          // Add up scores
          if (results[player].answers[question].correct) score++;
          individualResult = {};
          // Find the time it took the user to answer the question
          const timeAnswered = new Date(results[player].answers[question].answeredAt)
          const timeStarted = new Date(results[player].answers[question].questionStartedAt)
          individualResult[questionNum] = {
            correct: results[player].answers[question].correct,
            timeTaken: ((timeAnswered - timeStarted) / 1000)
          }
          // This is used to setup the array in the responseTimes object
          if (firstRun) {
            responseTimes[questionNum] = [(timeAnswered - timeStarted) / 1000]
          } else {
            responseTimes[questionNum].push((timeAnswered - timeStarted) / 1000);
          }
          // Add the player's results in the question to the resultArray
          resultArray.push(individualResult);
          questionNum++;
        }
        firstRun = false;
        // Add all of the player's results to the gameResult object
        gameResult[results[player].name] = resultArray;
        // Add their scores too
        scores[results[player].name] = score;
        score = 0;
        resultArray = [];
      }
      const responseTimeAverageCopy = [];
      // Find the average of the response times
      for (const questionNum in responseTimes) {
        const sum = responseTimes[questionNum].reduce((total, num) => total + num, 0);
        // Add it to the array
        responseTimeAverageCopy.push({ question: questionNum, averageTime: sum / responseTimes[questionNum].length });
      }
      // Set the responseTimeAverage state
      setResponseTimeAverage(responseTimeAverageCopy)
      const tableRowsCopy = [];
      let id = 1;
      for (const playerName in scores) {
        if (id === 6) break;
        const score = scores[playerName];
        tableRowsCopy.push({ id, playerName, score });
        id++;
      }
      setTableRows(tableRowsCopy);
    } catch (error) {
      console.log(error);
    }
  }, []);

  // Columns for the results table
  const tableColumns = [
    { field: 'playerName', headerName: 'Player Name', width: 200 },
    { field: 'score', headerName: 'Score', type: 'number', sortable: true, width: 200 },
  ]

  return (
    <Grid container direction="column" spacing={2} style={entireStyle}>
      <Typography style={topStyle} variant="h5">{props.message}</Typography>
      <Typography variant="h4"><b>Results</b></Typography>
      <hr style={lineStyle}/>
      <Grid item xs={12} align='center'>
        <Typography variant="h6"><i>Top 5 Scores</i></Typography>
        <div style={{ width: '100%' }}>
          <DataGrid rows={tableRows} columns={tableColumns} hideFooter autoHeight sortModel={[
            {
              field: 'score',
              sort: 'desc',
            },
          ]}></DataGrid>
        </div>
      </Grid>
      <Grid item xs={12} align='center'>
        <Typography variant="h6"><i>Average Response Times For Each Question</i></Typography>
        <BarChart width={450} height={300} data={responseTimeAverage}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="question" />
          <YAxis />
          <Tooltip />
          <Bar dataKey="averageTime" fill="#afc9ed" />
        </BarChart>
      </Grid>
    </Grid>
  )
}

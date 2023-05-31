import React from 'react'
import { useParams } from 'react-router-dom';
import { fetchSetup } from './helper.js';
import { Typography, Stack } from '@mui/material';

export default function ViewGameResults () {
  const params = useParams();
  const [results, setResults] = React.useState('');
  React.useEffect(async () => {
    try {
      const details = await fetchSetup('GET', `play/${params.playerId}/results`, null, null);
      let correct = 0;
      details.forEach(elem => {
        if (elem.correct) {
          correct++;
        }
      })
      const string = `You got ${correct} correct out of ${details.length}!`
      setResults(string);
      console.log(details);
      console.log(results);
    } catch (error) {
      console.log(error);
    }
  }, [])
  return (
    <main>
      <Stack alignItems='center'>
          <Typography variant='h4'>Results</Typography>
          <Typography>
            {results}
          </Typography>
      </Stack>
    </main>
  )
}

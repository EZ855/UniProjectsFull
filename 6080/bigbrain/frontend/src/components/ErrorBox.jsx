import React from 'react';
import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';

const errorStyle = {
  width: '50%',
  position: 'fixed',
  top: '100px',
  left: '50%',
  transform: 'translateX(-50%)',
  zIndex: '10',
  fontSize: '13pt'
}

export default function ErrorBox (props) {
  const { errorMessage, closeError } = props;

  return (
        <Alert
          id={'error-' + errorMessage}
          style={errorStyle}
          onClose={closeError}
          severity="error"
          role="alert"
        >
            <AlertTitle><strong>Error</strong></AlertTitle>
            {errorMessage}
        </Alert>
  )
}

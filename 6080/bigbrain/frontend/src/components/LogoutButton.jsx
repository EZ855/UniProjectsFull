import React from 'react'
import Button from '@mui/material/Button';

function LogoutButton ({ color, variant, text, onClick }) {
  if (color === undefined) {
    color = 'primary';
    variant = 'contained';
    text = 'Default'
    onClick = () => {}
  }
  return (
    <Button
      variant={variant}
      color={color}
      onClick={onClick}
    >{text}</Button>
  )
}

export default LogoutButton

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, TextField, Container, Typography, Box } from '@mui/material';
import axios from 'axios';
import API from '../config/api';

const Login = ({ onLoginSuccess }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const response = await axios.post(API.LOGIN, {
        username,
        password
      });

      if (response.status === 200) {
        const token = response.data.token;
        localStorage.setItem('token', token);
        localStorage.setItem('username', username);

        // Fetch user ID
        const userResponse = await axios.get(API.GET_USER_BY_USERNAME(username), {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });

        if (userResponse.status === 200) {
          const userId = userResponse.data.id;
          localStorage.setItem('userId', userId);

          // Fetch operations
          const operationsResponse = await axios.get(API.GET_OPERATIONS, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });

          if (operationsResponse.status === 200) {
            localStorage.setItem('operations', JSON.stringify(operationsResponse.data));
            onLoginSuccess(userId, operationsResponse.data);
            navigate('/main'); // Navigate to the main page after successful login
          } else {
            alert('Failed to fetch operations');
          }
        } else {
          alert('Failed to fetch user ID');
        }
      } else {
        alert('Invalid credentials');
      }
    } catch (error) {
      console.error('Error during login:', error);
      if (error.response) {
        console.error('Response data:', error.response.data);
        console.error('Response status:', error.response.status);
        console.error('Response headers:', error.response.headers);
      } else if (error.request) {
        console.error('Request data:', error.request);
      } else {
        console.error('Error message:', error.message);
      }
      alert('Failed to login. Please check your credentials and try again.');
    }
  };

  return (
    <Container maxWidth="xs">
      <Box sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="h1" variant="h5">
          Login
        </Typography>
        <Box sx={{ mt: 1 }}>
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            label="Username"
            autoFocus
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            label="Password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Button
            fullWidth
            variant="contained"
            color="primary"
            sx={{ mt: 3, mb: 2 }}
            onClick={handleLogin}
          >
            Login
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default Login;
import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Main from './pages/Main';

function App() {
  const [userId, setUserId] = useState(null);
  const [operations, setOperations] = useState([]);

  const onLoginSuccess = (userId, operations) => {
    setUserId(userId);
    setOperations(operations);
  };

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login onLoginSuccess={onLoginSuccess} />} />
        <Route path="/main" element={<Main userId={userId} operations={operations} />} />
      </Routes>
    </Router>
  );
}

export default App;
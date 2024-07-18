import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Typography, Button, Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, TablePagination, TableSortLabel, TextField } from '@mui/material';
import axios from 'axios';
import AddRecordModal from '../components/AddRecordModal';
import API from '../config/api';

const Main = () => {
  const [records, setRecords] = useState([]);
  const [latestResult, setLatestResult] = useState(null);
  const [latestOperation, setLatestOperation] = useState(null);
  const [userBalance, setUserBalance] = useState(null);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [totalRows, setTotalRows] = useState(0);
  const [modalOpen, setModalOpen] = useState(false);
  const [sortField, setSortField] = useState('date');
  const [sortDirection, setSortDirection] = useState('desc');
  const [search, setSearch] = useState('');
  const navigate = useNavigate();
  const operations = JSON.parse(localStorage.getItem('operations')) || [];
  const username = localStorage.getItem('username');

  useEffect(() => {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      navigate('/');
      return;
    }
    fetchRecords(userId, page, rowsPerPage, sortField, sortDirection, search);
    fetchLatestData(userId);
  }, [page, rowsPerPage, sortField, sortDirection, search]);

  const fetchRecords = async (userId, page, rowsPerPage, sortField, sortDirection, search) => {
    const token = localStorage.getItem('token');
    try {
      const response = await axios.get(API.GET_RECORDS(userId), {
        params: {
          page,
          size: rowsPerPage,
          sortField,
          sortDirection,
          search
        },
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setRecords(response.data.content);
      setTotalRows(response.data.totalElements);
    } catch (error) {
      console.error('Error fetching records:', error);
      setRecords([]);
    }
  };

  const fetchLatestData = async (userId) => {
    const token = localStorage.getItem('token');
    try {
      const response = await axios.get(API.GET_RECORDS(userId), {
        params: {
          page: 0,
          size: 1,
          sortField: 'date',
          sortDirection: 'desc'
        },
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      if (response.data.content.length > 0) {
        const latestRecord = response.data.content[0];
        setLatestResult(latestRecord.operationResponse);
        setLatestOperation(`${getOperationLabel(latestRecord.operationId)}: ${latestRecord.amount}`);
        setUserBalance(latestRecord.userBalance);
      } else {
        setLatestResult(null);
        setLatestOperation(null);
        setUserBalance(null);
      }
    } catch (error) {
      console.error('Error fetching latest data:', error);
    }
  };

  const deleteRecord = async (id) => {
    const token = localStorage.getItem('token');
    try {
      await axios.delete(API.DELETE_RECORD(id), {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      const userId = localStorage.getItem('userId');
      fetchRecords(userId, page, rowsPerPage, sortField, sortDirection, search);
      fetchLatestData(userId);
    } catch (error) {
      console.error('Error deleting record:', error);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    localStorage.removeItem('operations');
    navigate('/');
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const handleOpenModal = () => {
    setModalOpen(true);
  };

  const handleCloseModal = () => {
    setModalOpen(false);
  };

  const handleRecordAdded = () => {
    const userId = localStorage.getItem('userId');
    fetchRecords(userId, page, rowsPerPage, sortField, sortDirection, search);
    fetchLatestData(userId);
  };

  const getOperationLabel = (operationId) => {
    const operation = operations.find(op => op.id === operationId);
    return operation ? operation.typeLabel : '';
  };

  const handleRequestSort = (property) => {
    const isAsc = sortField === property && sortDirection === 'asc';
    setSortDirection(isAsc ? 'desc' : 'asc');
    setSortField(property);
  };

  const handleSearchChange = (event) => {
    setSearch(event.target.value);
  };

  return (
    <Container>
      <Box sx={{ mt: 8 }}>
        <Box sx={{ mb: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div>
            <Button variant="contained" color="primary" sx={{ mr: 2 }} onClick={handleOpenModal}>
              Add Operation
            </Button>
          </div>
          <Typography variant="body1" sx={{ mr: 2 }}>
            {username}
          </Typography>
          <Button variant="contained" color="error" onClick={handleLogout}>
            Logout
          </Button>
        </Box>
        <Box sx={{ mb: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Box sx={{ flex: 1, p: 2, border: '1px solid gray', borderRadius: '8px', marginRight: '8px' }}>
            <Typography variant="h6">Latest Result</Typography>
            <Typography variant="body1">{latestResult}</Typography>
          </Box>
          <Box sx={{ flex: 1, p: 2, border: '1px solid gray', borderRadius: '8px', marginRight: '8px' }}>
            <Typography variant="h6">Last Operation</Typography>
            <Typography variant="body1">{latestOperation}</Typography>
          </Box>
          <Box sx={{ flex: 1, p: 2, border: '1px solid gray', borderRadius: '8px' }}>
            <Typography variant="h6">User Balance</Typography>
            <Typography variant="body1">{userBalance}</Typography>
          </Box>
        </Box>
        <TextField
          label="Search"
          value={search}
          onChange={handleSearchChange}
          fullWidth
          margin="normal"
        />
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sortDirection={sortField === 'operation' ? sortDirection : false}>
                  <TableSortLabel
                    active={sortField === 'operation'}
                    direction={sortField === 'operation' ? sortDirection : 'asc'}
                    onClick={() => handleRequestSort('operation')}
                  >
                    Operation
                  </TableSortLabel>
                </TableCell>
                <TableCell sortDirection={sortField === 'amount' ? sortDirection : false}>
                  <TableSortLabel
                    active={sortField === 'amount'}
                    direction={sortField === 'amount' ? sortDirection : 'asc'}
                    onClick={() => handleRequestSort('amount')}
                  >
                    Amount
                  </TableSortLabel>
                </TableCell>
                <TableCell sortDirection={sortField === 'userBalance' ? sortDirection : false}>
                  <TableSortLabel
                    active={sortField === 'userBalance'}
                    direction={sortField === 'userBalance' ? sortDirection : 'asc'}
                    onClick={() => handleRequestSort('userBalance')}
                  >
                    User Balance
                  </TableSortLabel>
                </TableCell>
                <TableCell sortDirection={sortField === 'operationResponse' ? sortDirection : false}>
                  <TableSortLabel
                    active={sortField === 'operationResponse'}
                    direction={sortField === 'operationResponse' ? sortDirection : 'asc'}
                    onClick={() => handleRequestSort('operationResponse')}
                  >
                    Operation Response
                  </TableSortLabel>
                </TableCell>
                <TableCell sortDirection={sortField === 'date' ? sortDirection : false}>
                  <TableSortLabel
                    active={sortField === 'date'}
                    direction={sortField === 'date' ? sortDirection : 'asc'}
                    onClick={() => handleRequestSort('date')}
                  >
                    Date
                  </TableSortLabel>
                </TableCell>
                <TableCell>
                  Actions
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {records.map((record) => (
                <TableRow key={record.id}>
                  <TableCell>{getOperationLabel(record.operationId)}</TableCell>
                  <TableCell>{record.amount}</TableCell>
                  <TableCell>{record.userBalance}</TableCell>
                  <TableCell>{record.operationResponse}</TableCell>
                  <TableCell>{record.date}</TableCell>
                  <TableCell>
                    <Button
                      variant="contained"
                      color="secondary"
                      onClick={() => deleteRecord(record.id)}
                    >
                      Delete
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25]}
          component="div"
          count={totalRows}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </Box>
      <AddRecordModal
        open={modalOpen}
        onClose={handleCloseModal}
        onRecordAdded={handleRecordAdded}
        operations={operations}  
      />
    </Container>
  );
};

export default Main;
import React, { useState, useEffect } from 'react';
import { Dialog, DialogActions, DialogContent, DialogTitle, Button, TextField, MenuItem, Select, InputLabel, FormControl } from '@mui/material';
import axios from 'axios';
import API from '../config/api';

const AddRecordModal = ({ open, onClose, onRecordAdded, operations }) => {
  const [operationId, setOperationId] = useState('');
  const [amount, setAmount] = useState('');
  const [hideAmount, setHideAmount] = useState(false);

  useEffect(() => {
    if (open) {
      setOperationId('');
      setAmount('');
    }
  }, [open]);

  const handleOperationChange = (e) => {
    const selectedOperationId = e.target.value;
    setOperationId(selectedOperationId);

    if (selectedOperationId === '2d121969-3a83-11ef-8ba6-0242ac120002' || selectedOperationId === '2d12ed7a-3a83-11ef-8ba6-0242ac120002') {
      setHideAmount(true);
    } else {
      setHideAmount(false);
    }
  };

  const handleAddRecord = async () => {
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');
    try {
      const response = await axios.post(API.ADD_RECORD, {
        username,
        operationId,
        amount: hideAmount ? null : parseFloat(amount)
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      if (response.status === 200) {
        onRecordAdded();
        onClose();
      } else {
        window.alert('Failed to add record.');
      }
    } catch (error) {
      console.error('Error adding record:', error);
      window.alert(error.response?.data || 'Failed to add record');
    }
  };

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Add New Operation</DialogTitle>
      <DialogContent>
        <FormControl fullWidth margin="dense">
          <InputLabel>Operation Type</InputLabel>
          <Select
            value={operationId}
            onChange={handleOperationChange}
            label="Operation Type"
          >
            {operations.map((operation) => (
              <MenuItem key={operation.id} value={operation.id}>
                {operation.typeLabel}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        {!hideAmount && (
          <TextField
            margin="dense"
            label="Amount"
            type="number"
            fullWidth
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
          />
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary">
          Cancel
        </Button>
        <Button onClick={handleAddRecord} color="primary">
          Add
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default AddRecordModal;
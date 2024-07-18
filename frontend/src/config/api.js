const LOGIN_BASE_URL = 'http://localhost:8082';
const USER_BASE_URL = 'http://localhost:8082/api/v1';
const CALCULATOR_BASE_URL = 'http://localhost:8081/api/v1';

const API = {
  LOGIN: `${LOGIN_BASE_URL}/auth/login`,
  GET_USER_BY_USERNAME: (username) => `${USER_BASE_URL}/users/${username}`,
  GET_RECORDS: (userId) => `${CALCULATOR_BASE_URL}/calculator/records?user_id=${userId}`,
  ADD_RECORD: `${CALCULATOR_BASE_URL}/calculator/operations/perform`,
  GET_OPERATIONS: `${CALCULATOR_BASE_URL}/calculator/operations`,
  DELETE_RECORD: (recordId) => `${CALCULATOR_BASE_URL}/calculator/records/${recordId}`
};

export default API;
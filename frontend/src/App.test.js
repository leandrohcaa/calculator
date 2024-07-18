import { render, screen } from '@testing-library/react';
import Login from './pages/Login';

test('renders login form', () => {
  renderWithRouter(<Login />);
  expect(screen.getByLabelText(/Username/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/Password/i)).toBeInTheDocument();
  expect(screen.getByText(/Login/i)).toBeInTheDocument();
});

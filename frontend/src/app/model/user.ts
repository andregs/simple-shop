export interface AuthenticatedUser {
  username: string;
  authorities: [{ authority: string }];
}

export interface Credentials {
  username: string;
  password: string;
}

export interface User {
  id: number;
  username: string;
  role: Role;
}

export type Role = 'ADMIN' | 'CUSTOMER';

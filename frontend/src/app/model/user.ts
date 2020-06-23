export interface User {
  username: string;
  authorities: [{ authority: string }];
}

export interface Credentials {
  username: string;
  password: string;
}

export type Role = 'ADMIN' | 'CUSTOMER';

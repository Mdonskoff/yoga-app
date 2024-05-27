import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { firstValueFrom } from 'rxjs';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it ('should verify log session ', async () => {
    const isLogged = await firstValueFrom(service.$isLogged());
    expect(isLogged).toBeFalsy();
  })

  it ('should log in a user ', () => {
    let user = {  
      token: "token",
      type: "type",
      id : 1,
      username : "yoan",
      lastName: "yoan",
      firstName: "test",
      admin : false,
    }
    
    service.logIn(user);

    expect(service.sessionInformation).toBe(user);
    expect(service.isLogged).toBeTruthy();
  })

  it ('should logout a user ', () => {

    service.logOut();
    
    expect(service.sessionInformation).toBeUndefined();
    expect(service.isLogged).toBeFalsy();
  })
});

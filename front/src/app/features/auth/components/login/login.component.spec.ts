import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {of, throwError} from "rxjs";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService : any;
  let sessionService : any;
  let fb: any;
  let router: any
  let form : any;

  beforeEach(async () => {
    authService = {
      login : jest.fn()
    }

    sessionService = {
      logIn : jest.fn()
    }

    fb =  {
      group : jest.fn()
    }

    form = jest.fn();

    router = jest.fn();

    component = new LoginComponent(authService, fb, router, sessionService)
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should throw an error when we call submit function', () => {
    component.form = form;
    const error = new Error('Error login test');
    //jest.mock('login', () => error);
    jest.spyOn(authService, 'login').mockReturnValue(throwError(() => error));

    component.submit();

    expect(authService.login).toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
  });

  it('should valid submit function', () => {
    component.form = form;
    const res = "test successful";
    jest.spyOn(authService, 'login').mockReturnValue(of(res));

    component.submit();

    expect(authService.login).toHaveBeenCalled();
    expect(component.onError).toBeFalsy();
  });

  afterEach(() => {
    jest.clearAllMocks();
    jest.resetAllMocks();
    jest.restoreAllMocks();
  });
});


import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import {TeacherService} from "../../../../services/teacher.service";
import {SessionApiService} from "../../services/session-api.service";
import {of} from "rxjs";


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let mockSessionApiService: any;
  let mockTeacherService: any;

  const mockSessionService = {
    sessionInformation: {
      id: 1,
      username: 'johndoe@test.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: true
    }
  };

  beforeEach(async () => {
    mockSessionApiService = {
      delete: jest.fn().mockReturnValue(of({})),
      detail: jest.fn().mockReturnValue(of({})),
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({}))
    };

    mockTeacherService = {
      detail: jest.fn().mockReturnValue(of({}))
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should delete on component deletion', () => {
    const sessionApiServiceDeleteSpy = jest.spyOn(mockSessionApiService, 'delete');
    component.delete();

    expect(sessionApiServiceDeleteSpy).toHaveBeenCalled();
  });

  it('should participate on participation', () => {
    const sessionApiServiceParticipateSpy = jest.spyOn(mockSessionApiService, 'participate');

    component.participate();

    expect(sessionApiServiceParticipateSpy).toHaveBeenCalled();
  });

  it('should unParticipate on withdrawal', () => {
    const sessionApiServiceUnParticipateSpy = jest.spyOn(mockSessionApiService, 'unParticipate');

    component.unParticipate();

    expect(sessionApiServiceUnParticipateSpy).toHaveBeenCalled();
  });
});

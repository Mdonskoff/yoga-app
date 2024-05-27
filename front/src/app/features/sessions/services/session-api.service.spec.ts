
import { expect } from '@jest/globals';
import { SessionApiService } from './session-api.service';
import { of } from 'rxjs';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpClientSpy : any;
  let session : Session;

  beforeEach(() => {
    
    httpClientSpy ={
      get : jest.fn(),
      post : jest.fn(),
      put : jest.fn(),
      delete : jest.fn(),
    }
    service = new SessionApiService(httpClientSpy);

    session = {
      id : 1,
      name : "session 1",
      description: "test",
      date: new Date(),
      teacher_id: 1,
      users: [1]
    }

  });

  afterEach(() => {
    // restore the spy created with spyOn
    jest.restoreAllMocks();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it ('should get all sessions', () => {
    const url = 'api/session';
    jest.spyOn(httpClientSpy, 'get').mockReturnValue(of<Session[]>(session));

    service.all();

    expect(httpClientSpy.get).toHaveBeenCalledWith(url);
  })

  it ('should get detail sessions', () => {
    const url = 'api/session/1';
    const id :string = '1';
    jest.spyOn(httpClientSpy, 'get').mockReturnValue(of<Session[]>(session));

    service.detail(id);

    expect(httpClientSpy.get).toHaveBeenCalledWith(url);
  })

  it ('should delete sessions', () => {
    const url = 'api/session/1';
    const id :string = '1';
    jest.spyOn(httpClientSpy, 'delete').mockReturnValue(of<any>(""));

    service.delete(id);

    expect(httpClientSpy.delete).toHaveBeenCalledWith(url);
  })

  it ('should create session', () => {
    const url = 'api/session';
    jest.spyOn(httpClientSpy, 'post').mockReturnValue(of<Session>(session));

    service.create(session);

    expect(httpClientSpy.post).toHaveBeenCalledWith(url, session);
  })

  it ('should update session', () => {
    const url = 'api/session/1';
    const id :string = '1';
    jest.spyOn(httpClientSpy, 'put').mockReturnValue(of<Session>(session));

    service.update(id, session);

    expect(httpClientSpy.put).toHaveBeenCalledWith(url, session);
  })

  it ('should participate at a session', () => {
    const url = 'api/session/1/participate/1';
    const id :string = '1';
    jest.spyOn(httpClientSpy, 'post').mockReturnValue(of<Session>(session));

    service.participate(id, id);

    expect(httpClientSpy.post).toHaveBeenCalledWith(url, null);
  })

  it ('should unparticipate at a session', () => {
    const url = 'api/session/1/participate/1';
    const id :string = '1';
    jest.spyOn(httpClientSpy, 'delete').mockReturnValue(of<Session>(session));

    service.unParticipate(id, id);

    expect(httpClientSpy.delete).toHaveBeenCalledWith(url);
  })
});

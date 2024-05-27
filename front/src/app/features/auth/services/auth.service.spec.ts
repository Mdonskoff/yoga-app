import { of } from "rxjs";
import { AuthService } from "./auth.service";
import { RegisterRequest } from "../interfaces/registerRequest.interface";
import { expect } from '@jest/globals';
import { LoginRequest } from "../interfaces/loginRequest.interface";

describe('AuthService', () => {
    let service: AuthService;
    let httpClientSpy : any;
    let register : RegisterRequest
    let login : LoginRequest

    beforeEach(() => {
        httpClientSpy = {
            post : jest.fn(),
        }
        service = new AuthService(httpClientSpy)
        
    })
    afterEach(() => {
        jest.restoreAllMocks();
      });

    it('should register user', () => {
        const url = 'api/auth/register';
        register =  {
            email: "test@example.com",
            firstName: "test",
            lastName: "test lastname",
            password: "test1234",
        }
        jest.spyOn(httpClientSpy, 'post').mockReturnValue(of());

        service.register(register);

        expect(httpClientSpy.post).toHaveBeenCalledWith(url, register);
    })

    it('should login user', () => {
        const url = 'api/auth/login';
        login =  {
            email: "test@example.com",
            password: "test1234",
        }
        jest.spyOn(httpClientSpy, 'post').mockReturnValue(of());

        service.login(login);

        expect(httpClientSpy.post).toHaveBeenCalledWith(url, login);
    })
})
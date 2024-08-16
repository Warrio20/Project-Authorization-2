import { AxiosResponse } from "axios";
import $api from "../httpHandler";
import { AuthResponse } from "../interfaces/responses/AuthResponse";

export default class AuthService {
    static async login(username: string, password: string): Promise<AxiosResponse<AuthResponse>>{
        return $api.post<AuthResponse>("/login", {username, password}).then(response => response);
    }
    static async register(username: string, password: string, email: string): Promise<AxiosResponse<AuthResponse>>{
        return $api.post<AuthResponse>("/registration", {username, email, password}).then(response => response);
    }
    static async logout(){
        return $api.post("/logout");
    }
    static async resetPassword(token: string, newPassword: string){
        return $api.post("/reset-password", {token, newPassword})
    }
}
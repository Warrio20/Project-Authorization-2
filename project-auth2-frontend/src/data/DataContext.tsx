import { createContext, FC, ReactNode, useState } from "react";
import { IUser } from "../interfaces/IUser";
import AuthService from "../services/AuthService";
import axios, { AxiosResponse } from "axios";
import { AuthResponse } from "../interfaces/responses/AuthResponse";
import { URL } from "../httpHandler";
interface IDataContext {
    user: IUser,
    isAuthorized: boolean,
    isLoading: boolean,
    isProcessing: boolean,
    register: (email: string, username: string, password: string) => void,
    login: (username: string, password: string) => void,
    refresh: () => void,
    logout: () => void,
    forgotPassword: (email: string) => Promise<any>;
    resetPassword: (uuid: string, newPassword: string) => Promise<any>
}
const DataContext = createContext<IDataContext | undefined>(undefined);
const DataProvider: FC<{ children: ReactNode }> = ({ children }) => {
    const [user, setUser] = useState({} as IUser);
    const [isAuthorized, setAuth] = useState(false);
    const [isLoading, setLoading] = useState(false);
    const [isProcessing, setProcess] = useState(false);
    const login = async (username: string, password: string) => {
        try {
            setProcess(true);
            const res = await AuthService.login(username, password);
            setProcess(false);
            localStorage.setItem("token", res.data.accessToken);
            setAuth(true);
            setUser(res.data.user);
        } catch(e: any) {
            setProcess(false)
            throw e;
        }
    }
    const register = async (username: string, email: string, password: string) => {
        try {
            setProcess(true);
            await AuthService.register(username, password, email);
            setProcess(false);
        } catch (e: any) {
            setProcess(false);
            throw e;
        }
    }
    const logout = async () => {
        await AuthService.logout();
        localStorage.removeItem("token");
        setAuth(false);
        setUser({} as IUser);
    }
    const refresh = async () => {
        setLoading(true);
        try {
            const res = await axios.get<AuthResponse>(`${URL}/refresh`, { withCredentials: true });
            localStorage.setItem("token", res.data.accessToken);
            setAuth(true);
            setUser(res.data.user);
        } catch (e: any) {
            console.log(e.response?.data?.message)
        }
        finally {
            setLoading(false);
        }
    }
    const forgotPassword = async (email: string): Promise<string | AxiosResponse> => {
        return AuthService.forgotPassword(email);
    }
    const resetPassword = async (uuid: string, newPassword: string): Promise<string | AxiosResponse> => {
        return await AuthService.resetPassword(uuid, newPassword);
    }
    return (
        <DataContext.Provider value={{ user, isAuthorized, isLoading, isProcessing, login, logout, register, refresh, forgotPassword, resetPassword }}>
            {children}
        </DataContext.Provider>
    )
}
export { DataProvider, DataContext }
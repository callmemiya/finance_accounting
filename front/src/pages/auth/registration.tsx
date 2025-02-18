import React, { useState } from 'react';
import { TextBox, Button } from 'devextreme-react';
import AuthService from '../../service/AuthService';
import { useNavigate } from 'react-router-dom';
import './auth.css';

export const Registration = () => {
    const navigate = useNavigate();
    const [state, setState] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
    });

    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        if (!validateForm()) {
            return;
        }

        setIsLoading(true);
        try {
            const response = await AuthService.register({
                username: state.username,
                email: state.email,
                password: state.password
            });

            // Сохраняем токен
            AuthService.setToken(response.token);
            
            // Перенаправляем на главную страницу
            navigate('/');
        } catch (error: any) {
            setError(error.message || 'Произошла ошибка при регистрации');
        } finally {
            setIsLoading(false);
        }
    };

    const validateForm = (): boolean => {
        if (!state.username) {
            setError('Логин обязателен');
            return false;
        }

        if (!state.email) {
            setError('Email обязателен');
            return false;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(state.email)) {
            setError('Неверный формат email');
            return false;
        }

        if (!state.password) {
            setError('Пароль обязателен');
            return false;
        }

        if (!state.confirmPassword) {
            setError('Подтверждение пароля обязательно');
            return false;
        }

        // Проверяем совпадение паролей
        if (state.password !== state.confirmPassword) {
            setError('Пароли не совпадают');
            return false;
        }

        return true;
    };

    const onUsernameChanged = (e: any) => {
        setState(prev => ({
            ...prev,
            username: e.value
        }));
    };

    const onEmailChanged = (e: any) => {
        setState(prev => ({
            ...prev,
            email: e.value
        }));
    };

    const onPasswordChanged = (e: any) => {
        setState(prev => ({
            ...prev,
            password: e.value
        }));
    };

    const onConfirmPasswordChanged = (e: any) => {
        setState(prev => ({
            ...prev,
            confirmPassword: e.value
        }));
    };

    return (
        <div className="auth-container">
            <div className="auth-form">
                <h2 className="text-center mb-4">Регистрация</h2>
                {error && (
                    <div className="alert alert-danger mb-3">
                        {error}
                    </div>
                )}
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <TextBox
                            value={state.username}
                            onValueChanged={onUsernameChanged}
                            placeholder="Логин"
                            inputAttr={{
                                'data-name': 'username'
                            }}
                            width="100%"
                        />
                    </div>

                    <div className="mb-3">
                        <TextBox
                            value={state.email}
                            onValueChanged={onEmailChanged}
                            placeholder="Email"
                            inputAttr={{
                                'data-name': 'email'
                            }}
                            width="100%"
                        />
                    </div>

                    <div className="mb-3">
                        <TextBox
                            mode="password"
                            value={state.password}
                            onValueChanged={onPasswordChanged}
                            placeholder="Пароль"
                            inputAttr={{
                                'data-name': 'password'
                            }}
                            width="100%"
                        />
                    </div>

                    <div className="mb-4">
                        <TextBox
                            mode="password"
                            value={state.confirmPassword}
                            onValueChanged={onConfirmPasswordChanged}
                            placeholder="Подтвердите пароль"
                            inputAttr={{
                                'data-name': 'confirmPassword'
                            }}
                            width="100%"
                        />
                    </div>

                    <Button
                        width="100%"
                        type="default"
                        useSubmitBehavior={true}
                        text={isLoading ? 'Регистрация...' : 'Зарегистрироваться'}
                        disabled={isLoading}
                    />

                    <div className="mt-3 text-center">
                        <a href="/login" className="text-primary">Уже есть аккаунт? Войти</a>
                    </div>
                </form>
            </div>
        </div>
    );
}; 
package com.proyecto.WalkDog.ui.login

import org.junit.jupiter.api.Assertions.*

import com.proyecto.WalkDog.data.model.LoginUiState
import com.proyecto.WalkDog.data.service.AccountService
import com.proyecto.WalkDog.data.service.LogService
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    // Mockeamos las dependencias
    private lateinit var accountService: AccountService
    private lateinit var logService: LogService

    // ViewModel bajo prueba
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        // Inicializamos los mocks
        accountService = mockk()
        logService = mockk(relaxed = true) // relaxed evita fallos en métodos no utilizados
        viewModel = LoginViewModel(accountService, logService)
    }

    @Test
    fun `onEmailChange actualiza el email en uiState`() {
        // Acción
        viewModel.onEmailChange("test@example.com")

        // Verificación
        assertEquals("test@example.com", viewModel.uiState.email)
    }

    @Test
    fun `onPasswordChange actualiza el password en uiState`() {
        // Acción
        viewModel.onPasswordChange("123456")

        // Verificación
        assertEquals("123456", viewModel.uiState.password)
    }

    @Test
    fun `onSignInClick muestra error si email o password están vacíos`() = runTest {
        // Configuración
        val onError = mockk<(String) -> Unit>(relaxed = true)

        // Acción
        viewModel.onSignInClick(onSuccess = {}, onError = onError)

        // Verificación
        verify { onError("Email and password cannot be empty.") }
    }

    @Test
    fun `onSignInClick llama a authenticate y actualiza el usuario si es exitoso`() = runTest {
        // Configuración
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val onError = mockk<(String) -> Unit>(relaxed = true)
        val email = "test@example.com"
        val password = "password123"

        // Simulamos el cambio de email y contraseña
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // Mock del método de autenticación
        coEvery { accountService.authenticate(email, password) } just Runs

        // Acción
        viewModel.onSignInClick(onSuccess = onSuccess, onError = onError)

        // Verificación
        coVerify { accountService.authenticate(email, password) }
        verify { onSuccess() }
    }

    @Test
    fun `onSignInClick llama onError si la autenticación falla`() = runTest {
        // Configuración
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val onError = mockk<(String) -> Unit>(relaxed = true)
        val email = "test@example.com"
        val password = "password123"

        // Simulamos el cambio de email y contraseña
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // Mock de un error en la autenticación
        coEvery { accountService.authenticate(email, password) } throws Exception("Invalid credentials")

        // Acción
        viewModel.onSignInClick(onSuccess = onSuccess, onError = onError)

        // Verificación
        coVerify { accountService.authenticate(email, password) }
        verify { onError("Login failed: Invalid credentials") }
    }
}
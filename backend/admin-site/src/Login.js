import React, {Component} from 'react';
import axios from 'axios';
import {CreateUser, DeleteUser, ShowImages} from './Panel'

/**
 * This class is used for the login component of the webpage
 * this is the first stop for the user where they must enter
 * an admin user name and password to continue
 */
class Login extends Component {
    /**
     * username: username inputted from form data
     * password: password inputted from form data
     * token: gather from calling /login after form submission
     * key: random key to reset value of file input
     * isLoggedIn: whether you have logged in or not
     * showCreateUser: whether to show the create user panel
     * showDeleteUser: whether to show the delete user panel
     *
     * Note: only 1 panel can show at 1 time
     */
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            token: '',
            key: Math.random(),
            file: null,
            isLoggedIn: false,
            showCreateUser: false,
            showDeleteUser: false,
            showImages: false
        };
        this.image = React.createRef();
    }

    /**
     * The following 3 methods are available only when the user has not logged in
     */

    /**
     * Handles the updating of username value from form field
     */
    handleUserNameChange = (event) => {
        this.setState({username: event.target.value});
    };

    /**
     * Handles the updating of password value from form field
     */
    handlePasswordChange = (event) => {
        this.setState({password: event.target.value});
    };

    /**
     * Gets called when the login form is submitted
     * first makes the login requested to the backend if successful will return a token else will display the error
     * second makes the admin_test request to make sure the user is an admin user, else it will display the error
     */
    handleSubmit = (event) => {
        axios.post("http://localhost:3000/login", {
            "username": this.state.username,
            "password": this.state.password
        }).then(res => {
            this.setState({token: res.data.token});
            axios.get("http://localhost:3000/admin_test", {
                headers: {
                    Authorization: this.state.token
                }
            }).then(res => {
                if (res.data.status === "Authorized") {
                    this.setState({isLoggedIn: true});
                }
            }).catch(error => {
                alert(error.response.data.status);
            });
        }).catch(error => {
            alert(error.response.data.status);
        });
        event.preventDefault();
    };

    /**
     * These functions are only available once the user has logged in with a valid admin account
     */

    /**
     * Called when the user clicks the Create User button
     * sets the showCreateUser value to true
     * set the showDeleteUser value to false
     */
    showCreateUser = () => {
        this.setState({showCreateUser: !this.state.showCreateUser, showDeleteUser: false, showImages: false});
    };

    /**
     * Called when the user clicks the Create User button
     * sets the showCreateUser value to true
     * set the showDeleteUser value to false
     */
    showDeleteUser = () => {
        this.setState({showCreateUser: false, showDeleteUser: !this.state.showDeleteUser, showImages: false});
    };

    /**
     * Called when the user clicks the Create User button
     * sets the showCreateUser value to true
     * set the showDeleteUser value to false
     */
    showImages = () => {
        this.setState({showCreateUser: false, showDeleteUser: false, showImages: !this.state.showImages});
    };

    /**
     * Takes file from input field and uploads to image endpoint
     * alerts user if there was an error or says File Uploaded
     */
    handleFileUpload = (event) => {
        event.preventDefault();
        let file = event.target.files[0];
        if (file != null) {
            let formData = new FormData();
            formData.append('file', file);
            axios.post('http://localhost:3000/image', formData, {
                headers: {
                    Authorization: this.state.token,
                    'Content-Type': 'multipart/form-data'
                }
            }).then(res => {
                alert(res.data.status);
                if (this.state.showImages) {
                    this.setState({showImages: false});
                    this.setState({showImages: true});
                }
                this.setState({key: Math.random()});
            }).catch(error => {
                alert(error.response.data.status);
                this.setState({key: Math.random()});
            });
        }
    };

    /**
     * Resets all state values which simulates you "logging" out
     */
    logout = () => {
        this.setState({
            username: '',
            password: '',
            token: '',
            key: Math.random(),
            isLoggedIn: false,
            showCreateUser: false,
            showDeleteUser: false,
            showImages: false
        });
    };

    render() {
        if (this.state.isLoggedIn) {
            return (
                <div>
                    <p>Logged in</p>
                    <p>Token: {this.state.token}</p>
                    <div>
                        <button onClick={this.showCreateUser}>Create User</button>
                        <button onClick={this.showDeleteUser}>Delete User</button>
                        <button onClick={this.showImages}>Images</button>
                        <button onClick={this.logout}>Logout</button>
                        {this.state.showCreateUser ? <CreateUser token={this.state.token}/> : null}
                        {this.state.showDeleteUser ? <DeleteUser token={this.state.token}/> : null}
                        {this.state.showImages ? <ShowImages token={this.state.token}/> : null}
                    </div>
                    <div>
                        <input type="file" key={this.state.key} onChange={this.handleFileUpload}/>
                    </div>
                </div>
            );
        } else {
            return (
                <div>
                    <form onSubmit={this.handleSubmit}>
                        <label>
                            User-name:
                            <input type="text" value={this.state.username} onChange={this.handleUserNameChange}/>
                        </label>
                        <label>
                            Password:
                            <input type="password" value={this.state.password} onChange={this.handlePasswordChange}/>
                        </label>
                        <input type="submit" value="Submit"/>
                    </form>
                </div>
            );
        }
    }
}

export default Login;

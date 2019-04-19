import React, {Component} from 'react';
import axios from "axios";

/**
 * This class is called when the user hits the Create User button after being logged in
 *
 * it allows users to create a user and receive an alert once created
 *
 * TODO: confirm password
 */
export class CreateUser extends Component {
    /**
     * username: username of user to create
     * password: password of user to create
     * passwordConfirm: copy of password
     */
    constructor(props) {
        super(props);
        this.state = {username: '', password: '', passwordConfirm: ''};
    }


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
     * Handles the updating of password confirm value from form field
     */
    handlePasswordConfirmChange = (event) => {
        this.setState({passwordConfirm: event.target.value});
    };

    /**
     * Called when the form is submitted
     * Makes a web request to /createuser
     * sends an alert with the status or an error of the status
     */
    handleSubmit = (event) => {
        if(this.state.password === this.state.passwordConfirm) {
            axios.post("http://localhost:3000/user", {
                "username": this.state.username,
                "password": this.state.password
            }, {
                headers: {
                    Authorization: this.props.token
                }
            }).then(res => {
                alert(res.data.status);
            }).catch(error => {
                alert(error.response.data.status);
            });
        }else
            alert("Passwords don't match");
        event.preventDefault();
    };

    render() {
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
                    <label>
                        Confirm Password:
                        <input type="password" value={this.state.passwordConfirm} onChange={this.handlePasswordConfirmChange}/>
                    </label>
                    <input type="submit" value="Submit"/>
                </form>
            </div>
        );
    }
}

/**
 * This class is called when the user hits the Delete user button after being successfully logged in
 *
 * this component shows all users and there is a delete button beside each user, click that button to delete the specific user
 *
 * Note: Admin users can't be deleted
 */
export class DeleteUser extends Component {
    /**
     * data: data loaded from componentDidMount, starts at null
     */
    constructor(props) {
        super(props);
        this.state = {data: null};
    }

    /**
     * Called on creation, or when manually invoked
     *
     * Loads all users from backend, puts them into a list if they're not an admin with a delete button
     */
    componentDidMount() {
        axios.get("http://localhost:3000/user", {
            headers: {
                Authorization: this.props.token
            }
        }).then(res => {
            let value = [];
            for (let i = 0; i < res.data.length; i++) {
                let temp = res.data[i];
                if (!temp.admin)
                    value.push(<li key={i}>Username: {temp.username}
                        <button onClick={() => this.deleteUser(temp)}>Delete</button>
                    </li>);
            }
            if (value.length === 0)
                value.push(<li key={0}>No Users to Delete</li>);
            this.setState({data: value});
        }).catch(error => {
            alert(JSON.stringify(error));
        });
    }

    /**
     * Called when the user clicks the delete button for a created user
     *
     * it makes a web request to /deleteuser with JSON data with key username
     */
    deleteUser = (event) => {
        axios.delete("http://localhost:3000/user/" + event.id, {
            headers: {
                Authorization: this.props.token
            }
        }).then(res => {
            alert(res.data.status);
            this.componentDidMount();
        }).catch(error => {
            alert(error);
        });
    };

    render() {
        return <ul>{this.state.data}</ul>;
    }
}

/**
 * This class is called when a user wants to see all images that are currently in circulation
 *
 * You will be able to view all images and hit the delete button to delete said image
 */
export class ShowImages extends Component {
    /**
     * data: array loaded from backend, contains all image ids
     */
    constructor(props) {
        super(props);
        this.state = {data: null};
    }

    /**
     * Loads all data from backend, array of hashs with id's of valid images
     */
    componentDidMount() {
        axios.get('http://localhost:3000/image', {
            headers: {
                Authorization: this.props.token
            }
        }).then(res => {
            let value = [];
            for (let i = 0; i < res.data.length; i++) {
                value.push(<li key={i}>{res.data[i].id} <a href={"http://localhost:3000/image/" + res.data[i].id}
                                                           target="_blank" rel="noopener noreferrer">Image
                    Link</a>
                    <button onClick={() => this.deleteImage(res.data[i].id)}>Delete</button>
                </li>)
            }
            if (res.data.length === 0)
                value.push(<li key={0}>No images are currently stored</li>);
            this.setState({data: value})
        })
    }

    /**
     * Called when user hits the delete button beside the image
     *
     * Deletes image from backend, based on id
     */
    deleteImage = (id) => {
        axios.delete('http://localhost:3000/image/' + id, {
            headers: {
                Authorization: this.props.token
            }
        }).then(res => {
            alert(res.data.status);
            this.componentDidMount();
        }).catch(error => {
            alert(error);
        });
    };

    render() {
        return <ul>{this.state.data}</ul>;
    }
}
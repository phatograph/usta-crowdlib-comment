let ItemBox = React.createClass({
  loadItemsFromServer: function() {
    $.ajax({
      url: this.state.url,
      dataType: 'json',
      cache: false,
      success: function(data) {
        this.setState({data: data});
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  handleItemSubmit: function(item) {
    $.ajax({
      url: this.state.url,
      dataType: 'json',
      contentType: 'application/json',
      type: 'POST',
      data: JSON.stringify(item),
      success: function(data) {
        this.loadItemsFromServer();
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  getInitialState: function() {
    return {
      pollInterval: 200,
      url: 'http://localhost:9998/items',
      data: []
    };
  },
  componentDidMount: function() {
    this.loadItemsFromServer();
    // setInterval(this.loadItemsFromServer, this.pollInterval);
  },
  render: function() {
    return (
        <div className="itemBox">
        <h1>Items</h1>
        <ItemList data={this.state.data} />
        <ItemForm onItemSubmit={this.handleItemSubmit} />
        </div>
        );
  }
});

let ItemList = React.createClass({
  render: function() {
    let itemNodes = this.props.data.map(function(item) {
      return (
          <Item title={item.title} id={item.id} key={item.id}>
          {`${item.user.name} ${item.user.surname}`}
          </Item>
          );
    });
    return (
        <div className="itemList">
        {itemNodes}
        </div>
        );
  }
});

let Item = React.createClass({
  render: function() {
    let link = `#/items/${this.props.id}`;
    return (
        <div className="item">
        <a href={link}><h2 className="">{this.props.title}</h2></a>
        {this.props.children}
        </div>
        );
  }
});

let ItemForm = React.createClass({
  getInitialState: function() {
    return {title: ''};
  },
  handleTitleChange: function(e) {
    this.setState({title: e.target.value});
  },
  handleSubmit: function(e) {
    e.preventDefault();
    let title = this.state.title.trim();
    if (!title) {
      return;
    }
    this.props.onItemSubmit({title: title});
    this.setState({title: ''});
  },
  render: function() {
    return (
        <form className="itemForm" onSubmit={this.handleSubmit}>
        <input
        type="text"
        placeholder="Title"
        value={this.state.title}
        onChange={this.handleTitleChange}
        />
        <input type="submit" value="Post" />
        </form>
        );
  }
});

let ItemInfo = React.createClass({
  loadItemsFromServer() {
    $.ajax({
      url: `${this.state.url}/${this.props.params.id}`,
      dataType: 'json',
      cache: false,
      success: function(data) {
        this.setState({data: data});
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  handleItemSubmit(x) {
    $.ajax({
      url: `${this.state.url}/${this.props.params.id}/reply`,
      dataType: 'json',
      contentType: 'application/json',
      type: 'POST',
      data: JSON.stringify(x),
      success: function(data) {
        this.loadItemsFromServer();
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  onFavourite(e) {
    e.preventDefault();

    $.ajax({
      url: `http://localhost:9998/comments/${e.target.rel}/favourite`,
      dataType: 'json',
      contentType: 'application/json',
      type: 'POST',
      success: function(data) {
        if (data) {
          this.loadItemsFromServer()
        }
      }.bind(this),
      error: function(xhr, status, err) {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },
  getInitialState() {
    return {
      url: 'http://localhost:9998/items',
      data: {
        item: {},
        comments: []
      }
    };
  },
  componentDidMount() {
    this.loadItemsFromServer();
  },
  render: function() {
    return (
        <div className="itemInfo">
        <h2>{this.state.data.item.title}</h2>
        <CommentList onFavourite={this.onFavourite} data={this.state.data.comments} />
        <CommentForm onItemSubmit={this.handleItemSubmit} />
        </div>
        );
  }
});

let CommentList = React.createClass({
  render() {
    let comments = this.props.data.map((comment) => (
          <div key={comment.id}>
          <div>
          {comment.content} – {comment.user.name} –
          {comment.date} – {comment.favourites} –
          <a href="#" rel={comment.id} onClick={this.props.onFavourite}>fav</a>
          </div>
          </div>
          ));
    return (
        <div className="commentList">
        {comments}
        </div>
        );
  }
});

let CommentForm = React.createClass({
  getInitialState: function() {
    return {content: ''};
  },
  handleContentChange: function(e) {
    this.setState({content: e.target.value});
  },
  handleSubmit: function(e) {
    e.preventDefault();
    let content = this.state.content.trim();
    if (!content) {
      return;
    }
    this.props.onItemSubmit({content: content});
    this.setState({content: ''});
  },
  render: function() {
    return (
        <form className="itemForm" onSubmit={this.handleSubmit}>
        <input
        type="text"
        placeholder="Content"
        value={this.state.content}
        onChange={this.handleContentChange}
        />
        <input type="submit" value="Post" />
        </form>
        );
  }
});

const App = React.createClass({
  render() {
    return (
      <div>
        <a href="#/items"><h1>App</h1></a>
        {this.props.children}
      </div>
    )
  }
})

let Route = window.ReactRouter.Route;
let Router = window.ReactRouter.Router;

ReactDOM.render((
  <Router>
    <Route path="/" component={App}>
      <Route path="/items" component={ItemBox} />
      <Route path="items/:id" component={ItemInfo} />
    </Route>
  </Router>
), document.getElementById('content'));
